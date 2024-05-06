echo "Compiling source code."
javac */*.java */*/*.java

echo "Distributing intermediate code to the different execution environments."

echo "  General Repository of Information"
rm -rf dirGeneralRepos
mkdir -p dirGeneralRepos dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/entities dirGeneralRepos/serverSide/sharedRegions \
         dirGeneralRepos/clientSide dirGeneralRepos/clientSide/entities dirGeneralRepos/commInfra
cp serverSide/main/ServerGeneralRepos.class dirGeneralRepos/serverSide/main
cp serverSide/entities/GeneralReposClientProxy.class dirGeneralRepos/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/GeneralRepos.class dirGeneralRepos/serverSide/sharedRegions
cp clientSide/entities/RefereeStates.class clientSide/entities/CoachStates.class clientSide/entities/ContestantStates.class dirGeneralRepos/clientSide/entities
cp commInfra/Semaphore.class commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ServerCom.class dirGeneralRepos/commInfra

echo "  Playground"
rm -rf dirPlayground
mkdir -p dirPlayground dirPlayground/serverSide dirPlayground/serverSide/main dirPlayground/serverSide/entities dirPlayground/serverSide/sharedRegions \
         dirPlayground/clientSide dirPlayground/clientSide/entities dirPlayground/clientSide/stubs dirPlayground/commInfra
cp serverSide/main/ServerGeneralRepos.class serverSide/main/ServerRefereeSite.class serverSide/main/ServerPlayground.class serverSide/main/ServerContestantsBench.class dirPlayground/serverSide/main
cp serverSide/entities/GeneralReposClientProxy.class serverSide/entities/PlaygroundClientProxy.class serverSide/entities/RefereeSiteClientProxy.class serverSide/entities/ContestantsBenchClientProxy.class dirPlayground/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/PlaygroundInterface.class serverSide/sharedRegions/Playground.class dirPlayground/serverSide/sharedRegions
cp clientSide/entities/RefereeStates.class clientSide/entities/ContestantStates.class clientSide/entities/CoachStates.class \
   dirPlayground/clientSide/entities
cp clientSide/stubs/ContestantsBenchStub.class clientSide/stubs/RefereeSiteStub.class clientSide/stubs/PlaygroundStub.class clientSide/stubs/GeneralReposStub.class dirPlayground/clientSide/stubs
cp commInfra/*.class dirPlayground/commInfra

echo "  RefereeSite"
rm -rf dirRefereeSite
mkdir -p dirRefereeSite dirRefereeSite/serverSide dirRefereeSite/serverSide/main dirRefereeSite/serverSide/entities dirRefereeSite/serverSide/sharedRegions \
         dirRefereeSite/clientSide dirRefereeSite/clientSide/entities dirRefereeSite/clientSide/stubs dirRefereeSite/commInfra
cp serverSide/main/ServerGeneralRepos.class serverSide/main/ServerRefereeSite.class serverSide/main/ServerPlayground.class serverSide/main/ServerContestantsBench.class dirRefereeSite/serverSide/main
cp serverSide/entities/GeneralReposClientProxy.class serverSide/entities/RefereeSiteClientProxy.class serverSide/entities/PlaygroundClientProxy.class serverSide/entities/ContestantsBenchClientProxy.class dirRefereeSite/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/RefereeSiteInterface.class serverSide/sharedRegions/RefereeSite.class dirRefereeSite/serverSide/sharedRegions
cp clientSide/entities/RefereeStates.class clientSide/entities/ContestantStates.class clientSide/entities/CoachStates.class \
   dirRefereeSite/clientSide/entities
cp clientSide/stubs/ContestantsBenchStub.class clientSide/stubs/RefereeSiteStub.class clientSide/stubs/PlaygroundStub.class clientSide/stubs/GeneralReposStub.class dirRefereeSite/clientSide/stubs
cp commInfra/*.class dirRefereeSite/commInfra

echo "  ContestantsBench"
rm -rf dirContestantsBench
mkdir -p dirContestantsBench dirContestantsBench/serverSide dirContestantsBench/serverSide/main dirContestantsBench/serverSide/entities dirContestantsBench/serverSide/sharedRegions \
         dirContestantsBench/clientSide dirContestantsBench/clientSide/entities dirContestantsBench/clientSide/stubs dirContestantsBench/commInfra
cp serverSide/main/ServerGeneralRepos.class serverSide/main/ServerRefereeSite.class serverSide/main/ServerContestantsBench.class serverSide/main/ServerPlayground.class dirContestantsBench/serverSide/main
cp serverSide/entities/GeneralReposClientProxy.class serverSide/entities/ContestantsBenchClientProxy.class serverSide/entities/RefereeSiteClientProxy.class serverSide/entities/PlaygroundClientProxy.class dirContestantsBench/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/ContestantsBenchInterface.class serverSide/sharedRegions/ContestantsBench.class dirContestantsBench/serverSide/sharedRegions
cp clientSide/entities/RefereeStates.class clientSide/entities/ContestantStates.class clientSide/entities/CoachStates.class \
   dirContestantsBench/clientSide/entities
cp clientSide/stubs/ContestantsBenchStub.class clientSide/stubs/RefereeSiteStub.class clientSide/stubs/PlaygroundStub.class clientSide/stubs/GeneralReposStub.class dirContestantsBench/clientSide/stubs
cp commInfra/*.class dirContestantsBench/commInfra


echo "  Referee"
rm -rf dirReferee
mkdir -p dirReferee dirReferee/serverSide dirReferee/serverSide/main dirReferee/clientSide dirReferee/clientSide/main dirReferee/clientSide/entities \
         dirReferee/clientSide/stubs dirReferee/commInfra
cp clientSide/main/ClientReferee.class dirReferee/clientSide/main
cp clientSide/entities/Referee.class clientSide/entities/RefereeStates.class dirReferee/clientSide/entities
cp clientSide/stubs/GeneralReposStub.class clientSide/stubs/PlaygroundStub.class clientSide/stubs/ContestantsBenchStub.class clientSide/stubs/RefereeSiteStub.class dirReferee/clientSide/stubs
cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class dirReferee/commInfra

echo "  Contestants"
rm -rf dirContestant
mkdir -p dirContestant dirContestant/serverSide dirContestant/serverSide/main dirContestant/clientSide dirContestant/clientSide/main dirContestant/clientSide/entities \
         dirContestant/clientSide/stubs dirContestant/commInfra
cp clientSide/main/ClientContestant.class dirContestant/clientSide/main
cp clientSide/entities/Contestant.class clientSide/entities/ContestantStates.class dirContestant/clientSide/entities
cp clientSide/stubs/GeneralReposStub.class clientSide/stubs/PlaygroundStub.class clientSide/stubs/ContestantsBenchStub.class clientSide/stubs/RefereeSiteStub.class dirContestant/clientSide/stubs
cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class dirContestant/commInfra

echo "  Coaches"
rm -rf dirCoach
mkdir -p dirCoach dirCoach/serverSide dirCoach/serverSide/main dirCoach/clientSide dirCoach/clientSide/main dirCoach/clientSide/entities \
         dirCoach/clientSide/stubs dirCoach/commInfra
cp clientSide/main/ClientCoach.class dirCoach/clientSide/main
cp clientSide/entities/Coach.class clientSide/entities/CoachStates.class dirCoach/clientSide/entities
cp clientSide/stubs/GeneralReposStub.class clientSide/stubs/PlaygroundStub.class clientSide/stubs/ContestantsBenchStub.class clientSide/stubs/RefereeSiteStub.class dirCoach/clientSide/stubs
cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class dirCoach/commInfra


echo "Compressing execution environments."
echo "  General Repository of Information"
rm -f  dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos
echo "  Playground"
rm -f  dirPlayground.zip
zip -rq dirPlayground.zip dirPlayground
echo "  RefereeSite"
rm -f  dirRefereeSite.zip
zip -rq dirRefereeSite.zip dirRefereeSite
echo "  ContestantsBench"
rm -f  dirContestantsBench.zip
zip -rq dirContestantsBench.zip dirContestantsBench
echo "  Contestant"
rm -f  dirContestant.zip
zip -rq dirContestant.zip dirContestant
echo "  Coach"
rm -f  dirCoach.zip
zip -rq dirCoach.zip dirCoach
echo "  Referee"
rm -f  dirReferee.zip
zip -rq dirReferee.zip dirReferee
echo "Deploying and decompressing execution environments."
mkdir -p /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build
rm -rf /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build/*
cp dirGeneralRepos.zip /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build/
cp dirRefereeSite.zip /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build/
cp dirContestantsBench.zip /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build/
cp dirPlayground.zip /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build/
cp dirContestant.zip /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build/
cp dirCoach.zip /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build/
cp dirReferee.zip /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build/
cd /home/dyxcvi/Documents/UAveiro/2023-2024/S2/SD/assignments/ASS2_T3G9/local_build/
unzip -q dirGeneralRepos.zip
unzip -q dirPlayground.zip
unzip -q dirRefereeSite.zip
unzip -q dirContestantsBench.zip
unzip -q dirCoach.zip
unzip -q dirContestant.zip
unzip -q dirReferee.zip
