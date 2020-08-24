local text = require("text")
local term = require("term")
local event = require("event")
local component = require("component")

local function isConnected()
  component.isAvailable('pos_terminal')
end

local function hasCard()
  component.pos_terminal.hasCard()
end

local function writeCard(key, value)
  component.pos_terminal.writeCard(key, value)
end

local function readCard(key)
  print(writeCard('test', 'test'))
  print(readCard('test'))
end

local function test()
  component.pos_terminal.hasCard()
end

print("Welcome to use WC Card Manager V1.0. Type 'help' for helps.")
while true do
  print("CM > ")
  local input = term.read()
  if input == "test" then
    test()
  elseif input == "exit" then
    break
  end
end
