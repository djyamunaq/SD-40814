port=22381
# Get PID(s) associated with the given port
pids=$(sshpass -f password ssh sd309@l040101-ws01.ua.pt lsof -ti :$port)
# Check if any PID(s) found
if [ -z "$pids" ]; then
    echo "No process found running on port $port"
else
# Kill the processes
echo "Killing processes running on port $port: $pids"
sshpass -f password ssh sd309@l040101-ws01.ua.pt kill $pids
fi

echo "Transfering data to the playground node."
sshpass -f password ssh sd309@l040101-ws01.ua.pt 'mkdir -p ASS2/Simulation'
sshpass -f password ssh sd309@l040101-ws01.ua.pt 'rm -rf ASS2/Simulation/*'
sshpass -f password scp dirPlayground.zip sd309@l040101-ws01.ua.pt:ASS2/Simulation
echo "Decompressing data sent to the playground node."
sshpass -f password ssh sd309@l040101-ws01.ua.pt 'cd ASS2/Simulation ; unzip -uq dirPlayground.zip'

echo "Executing program at the playground node."
sshpass -f password ssh sd309@l040101-ws01.ua.pt 'cd ASS2/Simulation/dirPlayground ; java serverSide.main.ServerPlayground 22381 l040101-ws05.ua.pt 22384'
