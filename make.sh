mkdir -p output
javac -Xlint:unchecked -d ./output/ ./*.java
cp manifest.mf ./output/
cd output
jar cfm botsong.jar manifest.mf *.class
rm manifest.mf
mv botsong.jar ../
cd ..
rm -rf output
