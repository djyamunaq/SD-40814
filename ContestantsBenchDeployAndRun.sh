port=22382
# Get PID(s) associated with the given port
pids=$(sshpass -f password ssh sd309@l040101-ws02.ua.pt lsof -ti :$port)
echo $pids
# Check if any PID(s) found
if [ -z "$pids" ]; then
    echo "No process found running on port $port"
else
# Kill the processes
echo "Killing processes running on port $port: $pids"
sshpass -f password ssh sd309@l040101-ws02.ua.pt kill $pids
fi

echo "Transfering data to the contestants bench node."
sshpass -f password ssh sd309@l040101-ws02.ua.pt 'mkdir -p ASS2/Simulation'
sshpass -f password ssh sd309@l040101-ws02.ua.pt 'rm -rf ASS2/Simulation/*'
sshpass -f password scp dirContestantsBench.zip sd309@l040101-ws02.ua.pt:ASS2/Simulation
echo "Decompressing data sent to the contestants bench node."
sshpass -f password ssh sd309@l040101-ws02.ua.pt 'cd ASS2/Simulation ; unzip -uq dirContestantsBench.zip'

echo "Executing program at the contestants bench node."
sshpass -f password ssh sd309@l040101-ws02.ua.pt 'cd ASS2/Simulation/dirContestantsBench ; java serverSide.main.ServerContestantsBench 22382 l040101-ws05.ua.pt 22384'
