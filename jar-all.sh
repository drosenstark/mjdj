export startDir=$PWD
cd ../MjdjAPI/bin
jar -cvf $startDir/MjdjApi.jar .
cd $startDir/bin
jar -cvf $startDir/Mjdj.jar .
cd $startDir


deployDirOsx="$HOME/Dropbox/dr2050-rig/MjdjMidiMorph"
deployDirWindows="../MjdjDeployment/MjdjWindows-Beta-Next"

# copy to Windows
cp *.jar "$deployDirWindows/lib"

echo "trying this: $deployDirOsx/Mjdj.app/Contents/Resources/Java/"
# move to OSX
mv *.jar "$deployDirOsx/Mjdj.app/Contents/Resources/Java/"

# remove the old morphs and devices
rm -rf $deployDirOsx/morphs
rm  -rf $deployDirOsx/morphs-groovy
rm -rf $deployDirOsx/devices


# copy the compiled morphs
cp -R ./morphs/ "$deployDirOsx/morphs"
cp -R ./morphs-groovy $deployDirOsx/
#cp -R ./devices/ ./MjdjMidiMorph/devices/


# repeat for Windows: remove the old morphs and devices
#rm -rf ./MjdjOnWindows/morphs/*
#rm  ./MjdjOnWindows/morphs-groovy
#rm -rf ./MjdjOnWindows/devices/*

# repeat for Windows: copy the compiled morphs
#cp -R ./morphs/ ./MjdjOnWindows/morphs/
#cp -R ./morphs-groovy ./MjdjOnWindows/
#cp -R ./devices/ ./MjdjOnWindows/devices/

