#!/bin/sh

#./gradlew assembleRelease

#
#funBuildMaibeiUser(){
#    sed -i "s/wandoujia/$1/g" MaiBaiUser/build.gradle
#    sed -i "s/CHANNEL_ID = 1001/CHANNEL_ID = $2/g" MaiBaiUser/src/main/java/com/maibai/user/constant/GlobalParams.java
#    ./gradlew assembleRelease
#
#    sed -i "s/$1/wandoujia/g" MaiBaiUser/build.gradle
#    sed -i "s/CHANNEL_ID = $2/CHANNEL_ID = 1001/g" MaiBaiUser/src/main/java/com/maibai/user/constant/GlobalParams.java
#}

#funBuildMaibeiUser   wandoujia     2001
#funBuildMaibeiUser   qq            2002
#funBuildMaibeiUser   91            2003
#funBuildMaibeiUser   huawei        2004
#funBuildMaibeiUser   xiaomi        2005
#funBuildMaibeiUser   baidu         2006
#funBuildMaibeiUser   anzhi         2007
#funBuildMaibeiUser   oppo          2008
#funBuildMaibeiUser   appchina      2009
#funBuildMaibeiUser   lenovo        2010
#funBuildMaibeiUser   meizu         2011
#funBuildMaibeiUser   baidusem      2012
#funBuildMaibeiUser   UCWeb         2013


rm -rf MaiBaiCash/build
funBuildMaibeiCash(){
    sed -i ''  "s/wandoujia/$1/g" MaiBaiCash/build.gradle
    sed -i '' "s/CHANNEL_ID = 2001/CHANNEL_ID = $2/g" MaiBaiCash/src/main/java/com/maibai/cash/constant/GlobalParams.java
    ./gradlew assembleRelease

    sed -i '' "s/$1/wandoujia/g" MaiBaiCash/build.gradle
    sed -i '' "s/CHANNEL_ID = $2/CHANNEL_ID = 2001/g" MaiBaiCash/src/main/java/com/maibai/cash/constant/GlobalParams.java
}

funBuildMaibeiCash   wandoujia     2001
funBuildMaibeiCash   qq            2002
funBuildMaibeiCash   91            2003
funBuildMaibeiCash   huawei        2004
funBuildMaibeiCash   xiaomi        2005
funBuildMaibeiCash   baidu         2006
funBuildMaibeiCash   anzhi         2007
funBuildMaibeiCash   oppo          2008
funBuildMaibeiCash   appchina      2009
funBuildMaibeiCash   lenovo        2010
funBuildMaibeiCash   meizu         2011
funBuildMaibeiCash   baidusem      2012
funBuildMaibeiCash   UCWeb         2013


