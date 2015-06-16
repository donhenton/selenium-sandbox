#this is a sample launching script for the screenshot batch system
#usage ./screenshot.sh source alpha       create the folder of initial images for comparison  use src/main/resources/alpha for configs
#usage ./screenshot.sh target beta          create the folder of images to compare to  use beta
#usage ./screenshot.sh compare fred        actually compare the images must specify env even though not used
mvn clean install -DskipTests=true exec:java -Dexec.mainClass="com.dhenton9000.screenshots.ScreenShotLauncher" -Dexec.classpathScope=runtime -P$2 -Dexec.args="--action=$1" 