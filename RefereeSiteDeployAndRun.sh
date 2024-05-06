port=22383
# Get PID(s) associated with the given port
pids=$(sshpass -f password ssh sd309@l040101-ws09.ua.pt lsof -ti :$port)
echo "pid $pids" 
# Check if any PID(s) found
if [ -z "$pids" ]; then
    echo "No process found running on port $port"
else
# Kill the processes
echo "Killing processes running on port $port: $pids"
sshpass -f password ssh sd309@l040101-ws09.ua.pt kill $pids
fi

echo "Transfering data to the referee site node."
sshpass -f password ssh sd309@l040101-ws09.ua.pt 'mkdir -p ASS2/Simulation'
sshpass -f password ssh sd309@l040101-ws09.ua.pt 'rm -rf ASS2/Simulation/*'
sshpass -f password scp dirRefereeSite.zip sd309@l040101-ws09.ua.pt:ASS2/Simulation
echo "Decompressing data sent to the referee site node."
sshpass -f password ssh sd309@l040101-ws09.ua.pt 'cd ASS2/Simulation ; unzip -uq dirRefereeSite.zip'

echo "Executing program at the referee site node."
sshpass -f password ssh sd309@l040101-ws09.ua.pt 'cd ASS2/Simulation/dirRefereeSite ; java serverSide.main.ServerRefereeSite 22383 l040101-ws05.ua.pt 22384'
