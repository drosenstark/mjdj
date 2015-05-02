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

rm -rf $HOME/MJDJ/morphs
ln -s $startDir/morphs $HOME/MJDJ/morphs
echo "Made it to $HOME/MJDJ/morphs"
rm -rf $HOME/MJDJ/morphs-groovy
ln -s $startDir/morphs-groovy $HOME/MJDJ/morphs-groovy
rm -rf $HOME/MJDJ/devices
ln -s $startDir/devices $HOME/MJDJ/devices

echo "If MJDJ itself has changed then you'll need"
echo "cd ../MjdjBundling"
echo "./make.sh"