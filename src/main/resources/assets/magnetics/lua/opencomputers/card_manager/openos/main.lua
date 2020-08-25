-- import
local text = require("text")
local thread = require("thread")
local event = require("event")
local component = require("component")
local computer = require("computer")
local term = require("term")

-- lib
local pos_terminal

local function connect(address)
  if component.type(address) == "pos_terminal" then
    pos_terminal = component.proxy(address)
    component.setPrimary("pos_terminal", address)
    return true
  end
  return false
end

local function disconnect()
  pos_terminal = nil
end

local function isConnected()
  return pos_terminal ~= nil
end

local function hasCard()
  return pos_terminal.hasCard()
end

local function writeCard(key, value)
  return pos_terminal.writeCard(key, value)
end

local function readCard(key)
  return pos_terminal.readCard(key)
end

-- utils
function printp(message)
  local x, y = term.getCursor()
  term.setCursor(1, y)
  print(message)
  io.write("CM > ")
end

-- event handler
function unknownEvent(...)
  print("Unknown event, report the bug to author.")
end
local eventHandler = setmetatable({}, { __index = function() return unknownEvent end })
function eventHandler.interrupted()
  os.exit(0);
end
function eventHandler.component_added(address, componentType)
  if componentType == "pos_terminal" then
    if not isConnected() then
      printp("Try to connected to " .. address .. " automatically.")
      if connect(address) then
        printp("Connect succeeded.")
      else
        printp("Connect failed.")
      end
    end
  end
end
function eventHandler.component_removed(address, componentType)
  if componentType == "pos_terminal" then
    if isConnected() and address == pos_terminal.address then
      disconnect()
      printp("Disconnect from " .. address)
    end
  end
end

function handleEvent(eventid, ...)
  if eventid then
    eventHandler[eventid](...)
  end
end
-- command list
local commands = {
  "connect",
  "disconnect",
  "status",
  "write",
  "read",
  "help",
  "exit"
  }
-- command handler
local function commandHandler(name, args)
  if name == "connect" then
    local address = nil
    -- get address
    if #args == 0 then
      if component.isAvailable("pos_terminal") then
        print("0. Exit")
        local devices = {}
        local i = 0
        for address, componentType in component.list("pos_terminal", true) do
          i = i + 1
          devices[i] = address
          print(i .. ". " .. address)
        end
        ::connect_input::
        term.write("Choose a number to connect: ")
        local number = tonumber(io.read())
        if number then
          if number <= 0 then
            return
          elseif number <= #devices then
            address = devices[number]
          else
            goto connect_input
          end
        else
          goto connect_input
        end
      else
        print("No available devices.")
        return
      end
    else
      address = component.get(args[1], "pos_terminal")
      if address == nil then
        print("No such device.")
        return
      end
    end
    -- connect
    if connect(address) then
      print("Connect succeeded.")
    else
      print("Connect failed.")
    end
  elseif name == "disconnect" then
    if isConnected() then
      disconnect()
      print("Disconnect succeeded.")
    else
      print("Not connected.")
    end
  elseif name == "status" then
    if isConnected() then
      print("Is connected to " .. pos_terminal.address)
      if hasCard() then
        print("Has card.")
      else
        print("No card.")
      end
    else
      print("Not connected.")
    end
  elseif name == "write" then
    if isConnected() then
      if hasCard() then
        writeCard(args[1], args[2])
        print("Success.")
      else
        print("No card.")
      end
    else
      print("Not connected.")
    end
  elseif name == "read" then
    if isConnected() then
      if hasCard() then
        print(readCard(args[1]))
      else
        print("No card.")
      end
    else
      print("Not connected.")
    end
  elseif name == "test" then
    print("Command: " .. name)
    print("Arguments: " .. table.concat(args, ", "))
    print("Arg count: " .. #args)
    print("===============================")
  elseif name == "help" or name == "?" then
    print("Available commands: ")
    print(table.concat(commands, "\n"))
  elseif name == "exit" or name == "quit" or name == "close" then
    print("Thanks for your using, goodbye!")
    os.exit(0)
  else
    print("Unknown command. Type 'help' for helps.")
  end
end
-- command history
local history = {}
-- main loop
local event_thread = thread.create(
  function()
    while true do
      handleEvent(event.pullMultiple("interrupted", "component_added", "component_removed"))
    end
  end
)
print("Welcome to use WC Card Manager V1.0 for OpenOS. Type 'help' for helps.")
computer.beep()
while true do
  term.write("CM > ")
  local line = text.trim(term.read(history, true, commands))
  if line ~= "" then
    local strings = text.tokenize(line)
    local name = table.remove(strings, 1)
    computer.beep()
    commandHandler(name, strings)
  end
end
