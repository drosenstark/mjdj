export startDir=$PWD
cd ../MjdjAPI/bin
jar -cvf $startDir/MjdjApi.jar .
cd $startDir/bin
jar -cvf $startDir/Mjdj.jar .
cd $startDir
# copy to Windows
cp *.jar ./MjdjOnWindows/lib
# move to OSX
mv *.jar ./MjdjMidiMorph/Mjdj.app/Contents/Resources/Java/


# remove the old morphs and devices
rm -rf ./MjdjMidiMorph/morphs/*
rm  ./MjdjMidiMorph/morphs-groovy
rm -rf ./MjdjMidiMorph/devices/*


# copy the compiled morphs
cp -R ./morphs/ ./MjdjMidiMorph/morphs/
cp -R ./morphs-groovy ./MjdjMidiMorph/
#cp -R ./devices/ ./MjdjMidiMorph/devices/

# repeat for Windows: remove the old morphs and devices
#rm -rf ./MjdjOnWindows/morphs/*
#rm  ./MjdjOnWindows/morphs-groovy
#rm -rf ./MjdjOnWindows/devices/*

# repeat for Windows: copy the compiled morphs
#cp -R ./morphs/ ./MjdjOnWindows/morphs/
#cp -R ./morphs-groovy ./MjdjOnWindows/
#cp -R ./devices/ ./MjdjOnWindows/devices/

