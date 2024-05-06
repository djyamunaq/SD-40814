pkill -f xterm
sleep 1
xterm  -T "General Repository" -hold -e "./GeneralReposDeployAndRun.sh" &
xterm  -T "Playground" -hold -e "./PlaygroundDeployAndRun.sh" &
xterm  -T "Referee Site" -hold -e "./RefereeSiteDeployAndRun.sh" &
xterm  -T "Contestants Bench" -hold -e "./ContestantsBenchDeployAndRun.sh" &
sleep 1
xterm  -T "Referee" -hold -e "./RefereeDeployAndRun.sh" &
xterm  -T "Coach" -hold -e "./CoachesDeployAndRun.sh" &
xterm  -T "Contestant" -hold -e "./ContestantsDeployAndRun.sh" &
