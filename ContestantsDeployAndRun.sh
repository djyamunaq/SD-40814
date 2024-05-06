echo "Transfering data to the contestants node."
sshpass -f password ssh sd309@l040101-ws06.ua.pt 'mkdir -p ASS2/Simulation'
sshpass -f password ssh sd309@l040101-ws06.ua.pt 'rm -rf ASS2/Simulation/*'
sshpass -f password scp dirContestant.zip sd309@l040101-ws06.ua.pt:ASS2/Simulation
echo "Decompressing data sent to the contestants node."
sshpass -f password ssh sd309@l040101-ws06.ua.pt 'cd ASS2/Simulation ; unzip -uq dirContestant.zip'
echo "Executing program at the contestants node."
sshpass -f password ssh sd309@l040101-ws06.ua.pt 'cd ASS2/Simulation/dirContestant ; java clientSide.main.ClientContestant l040101-ws09.ua.pt 22383 l040101-ws02.ua.pt 22382 l040101-ws01.ua.pt 22381 l040101-ws05.ua.pt 22384'
