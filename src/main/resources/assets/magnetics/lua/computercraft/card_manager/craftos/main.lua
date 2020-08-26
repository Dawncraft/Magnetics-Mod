-- lib
local pos_terminal

local function isConnected()
  peripheral.isPresent(pos_terminal)
end

local function hasCard()
  peripheral.call(pos_terminal, 'hasCard')
end

local function writeCard(key, value)
  peripheral.call(pos_terminal, 'writeCard', 'test', 'test')
end

local function readCard(key)
  peripheral.call(pos_terminal, 'readCard', 'test')
end

local function test()
  print(hasCard())
  print(writeCard('test', 'test'))
  print(readCard('test'))
end

-- Main
print("Welcome to use WC Card Manager V1.0 for CraftOS. Type 'help' for helps.")
-- main loop
while true do
  write("CM > ")
  local command = read(nil, {})
end
