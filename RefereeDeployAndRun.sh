echo "Transfering data to the referee node."
sshpass -f password ssh sd309@l040101-ws08.ua.pt 'mkdir -p ASS2/Simulation'
sshpass -f password ssh sd309@l040101-ws08.ua.pt 'rm -rf ASS2/Simulation/*'
sshpass -f password scp dirReferee.zip sd309@l040101-ws08.ua.pt:ASS2/Simulation
echo "Decompressing data sent to the referee node."
sshpass -f password ssh sd309@l040101-ws08.ua.pt 'cd ASS2/Simulation ; unzip -uq dirReferee.zip'
echo "Executing program at the referee node."
sshpass -f password ssh sd309@l040101-ws08.ua.pt 'cd ASS2/Simulation/dirReferee ; java clientSide.main.ClientReferee l040101-ws09.ua.pt 22383 l040101-ws02.ua.pt 22382 l040101-ws01.ua.pt 22381 l040101-ws05.ua.pt 22384'
