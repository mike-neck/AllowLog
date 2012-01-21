#!/usr/bin/env ruby

require "xmlrpc/client"
require "net/https"
require "openssl"

class Backlog
  def initialize(config)
    @client = XMLRPC::Client.new("#{config["space"]}.backlog.jp", "/XML-RPC", 443, nil, nil, config["user"], config["password"], true ,nil)
  end

  def post_commit(log)
    issue = @client.call("backlog.getIssue", log[:key])
    return unless issue

    message = "#{log[:comment]}" + (log[:commit] && "\n#{log[:commit]}")
    if log[:status] && issue["status"]["id"] < log[:status]
      @client.call("backlog.switchStatus", {
          :key => log[:key],
          :statusId => log[:status],
          :comment => message
        })
    else
      @client.call("backlog.addComment", {
          :key => log[:key],
          :content => message
        })
    end
  end
end

config = {}
open("|git config --list | grep 'backlog.*=*'").each_line do |line|
  config[$1] = $2 if line =~ /backlog\.(\w+)=([^\s]+)/
end
open("|git config --list | grep remote.origin.url").each_line do |line|
  config["commit_url"] = "https://github.com/#{$1}/commit" if line =~ /remote\.origin\.url=git@github\.com:(.+)\.git/
end

if config.keys.size < 4
  puts "[ERROR] github2backlog-commit:"
  puts "  git config backlog.space [space]"
  puts "  git config backlog.user [username]"
  puts "  git config backlog.password [password]"

  exit(1)
end

log = {}
open("|git log -1 HEAD") do |io|
  while (line = io.gets) != nil
    case io.lineno
    when 1
      log[:commit] = "#{config['commit_url']}/#{line.split(/\s/)[1]}" if config["commit_url"]
    when 2
      log[:author] = $1 if line.match(/Author:\s+(.+)/)
    when 3
      log[:date] = $1 if line.match(/Date:\s+(.+)/)
    else
      line.strip!

      log[:key] = $1 if line.match(/(\w+\-\d+)/)
      log[:status] = 3 if line.match(/(#fix|#fixes|#fixed)/)
      log[:status] = 4 if line.match(/(#close|#closes|#closed)/)
      (log[:comment] ||= "") << line + $/ if line != ""
    end
  end
end

unless log[:key]
  exit(0)
end

backlog = Backlog.new(config)
backlog.post_commit(log)
