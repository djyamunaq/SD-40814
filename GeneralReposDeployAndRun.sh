port=22384
# Get PID(s) associated with the given port
pids=$(sshpass -f password ssh sd309@l040101-ws05.ua.pt lsof -ti :$port)
# Check if any PID(s) found
if [ -z "$pids" ]; then
    echo "No process found running on port $port"
else
# Kill the processes
echo "Killing processes running on port $port: $pids"
sshpass -f password ssh sd309@l040101-ws05.ua.pt kill $pids
fi

echo "Transfering data to the general repository node."
sshpass -f password ssh sd309@l040101-ws05.ua.pt 'mkdir -p ASS2/Simulation'
sshpass -f password ssh sd309@l040101-ws05.ua.pt 'rm -rf ASS2/Simulation/*'
sshpass -f password scp dirGeneralRepos.zip sd309@l040101-ws05.ua.pt:ASS2/Simulation
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd309@l040101-ws05.ua.pt 'cd ASS2/Simulation ; unzip -uq dirGeneralRepos.zip'

echo "Executing program at the server general repository."
sshpass -f password ssh sd309@l040101-ws05.ua.pt 'cd ASS2/Simulation/dirGeneralRepos ; java serverSide.main.ServerGeneralRepos 22384'
echo "Server shutdown."
sshpass -f password ssh sd309@l040101-ws05.ua.pt 'cd ASS2/Simulation/dirGeneralRepos ; less logger'
