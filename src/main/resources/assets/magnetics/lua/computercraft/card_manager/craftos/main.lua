local function isConnected()
  peripheral.isPresent('up')
end

local function hasCard()
  peripheral.call('up', 'hasCard')
end

local function writeCard(key, value)
  peripheral.call('up', 'writeCard', 'test', 'test')
end

local function readCard(key)
  peripheral.call('up', 'readCard', 'test')
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
