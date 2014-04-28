export startDir=$PWD
cd ../MjdjAPI/bin
jar -cvf $startDir/MjdjApi.jar .
cd $startDir/bin
jar -cvf $startDir/Mjdj.jar .
cd $startDir


deployDirOsxJars="/dans-data/java/MjdjBundling/bin"
deployDirOsxMorphs="/dans-data/java/MjdjBundling/dist"
deployDirWindows="../MjdjDeployment/MjdjWindows-Beta-Next"

# copy to Windows
cp *.jar "$deployDirWindows/lib"

echo "trying this: $deployDirOsxJars/"
# move to OSX
mv *.jar "$deployDirOsxJars/"

# remove the old morphs and devices
#rm -rf $deployDirOsxMorphs/morphs
#rm -rf $deployDirOsxMorphs/morphs-groovy
#rm -rf $deployDirOsxMorphs/devices


# copy the compiled morphs
#cp -R ./morphs/ "$deployDirOsxMorphs/morphs"
#cp -R ./morphs-groovy $deployDirOsxMorphs/
#cp -R ./devices/ ./MjdjMidiMorph/devices/


# repeat for Windows: remove the old morphs and devices
#rm -rf ./MjdjOnWindows/morphs/*
#rm  ./MjdjOnWindows/morphs-groovy
#rm -rf ./MjdjOnWindows/devices/*

# repeat for Windows: copy the compiled morphs
#cp -R ./morphs/ ./MjdjOnWindows/morphs/
#cp -R ./morphs-groovy ./MjdjOnWindows/
#cp -R ./devices/ ./MjdjOnWindows/devices/

rm -rf $HOME/MJDJ/morphs
ln -s $startDir/morphs $HOME/MJDJ/morphs
echo "Made it to $HOME/MJDJ/morphs"
rm -rf $HOME/MJDJ/morphs-groovy
ln -s $startDir/morphs-groovy $HOME/MJDJ/morphs-groovy
rm -rf $HOME/MJDJ/devices
ln -s $startDir/devices $HOME/MJDJ/devices
#ln -s $startDir/morphs-groovy $HOME/MJDJ/morphs-groovy

echo "If MJDJ itself has changed then you'll need"
echo "cd ../MjdjBundling"
echo "./make.sh"