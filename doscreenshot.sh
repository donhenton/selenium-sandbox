#this is a sample launching script for the screenshot batch system
#usage ./doscreenshot.sh source alpha       create the folder of initial images for comparison  use src/main/resources/alpha for configs
#usage ./doscreenshot.sh target beta          create the folder of images to compare to  use beta
#usage ./doscreenshot.sh compare fred        actually compare the images must specify env even though not used
#usage ./doscreenshot.sh source alpha phantomjs same as line 1 but using phantomjs remote server
mvn clean install -DskipTests=true exec:java -Dexec.mainClass="com.dhenton9000.screenshots.ScreenShotLauncher" -Dexec.classpathScope=runtime -P$2 -Dremote.server=$3 -Dexec.args="--action=$1" 