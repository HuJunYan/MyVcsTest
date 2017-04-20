#!/bin/sh
currentApplicationName='TianShenCash'

currentApkTrailName="'_$currentApplicationName\_1-0-4_04201033.apk'"
currentVersionCode="104"
currentVersionName='"1.0.4"'


funChangeVersionCode() {
    versionCode=`grep  "versionCode"  $currentApplicationName/build.gradle`
      lastVersionCode=`echo $versionCode| cut -d \  -f 2`
        sed -i ''  "s/$lastVersionCode/$currentVersionCode/g" $currentApplicationName/build.gradle
#  echo "versionCode change from " $lastVersionCode " to " $currentVersionCode "successful"
}
funChangeVersionName() {
    versionName=`grep  "versionName"  $currentApplicationName/build.gradle`
      lastVersionName=`echo $versionName| cut -d \  -f 2`
        sed -i ''  "s/$lastVersionName/$currentVersionName/g" $currentApplicationName/build.gradle
#  echo "versionName change from " $lastVersionName " to " $currentVersionName  "successful"
}
funChangeOutApkName() {
    data=`grep  "def releaseApkName"  $currentApplicationName/build.gradle`
      lastApkTrailName=`echo $data| cut -d \+ -f 2`
        sed -i ''  "s/$lastApkTrailName/$currentApkTrailName/g" $currentApplicationName/build.gradle
#  echo "apkTrailName change from " $lastApkTrailName " to " $currentApkTrailName  "successful"
}
CHANNEL_FILE_PATH_TIANSHEN_CASH='TianShenCash/src/main/java/com/tianshen/cash/constant/GlobalParams.java'
CHANNEL_FILE_PATH_USER='MaibeiUser/src/main/java/com/maibai/user/constant/GlobalParams.java'
CHANNEL_FILE_PATH_MERCHANT='MaiBaiMerchants/src/main/java/com/maibei/merchants/constant/GlobalParams.java'
CHANNEL_FILE_PATH_MAIBEI_CASH='MaiBaiCash/src/main/java/com/maibai/cash/constant/GlobalParams.java'

funFindAndChangeChannel() {
  case $currentApplicationName in
       TianShenCash)
       funFindAndChangeInt $1 $2 $CHANNEL_FILE_PATH_TIANSHEN_CASH
       ;;
       MaibeiUser)
       funFindAndChangeInt $1 $2 $CHANNEL_FILE_PATH_USER
       ;;
       MaiBaiMerchants)
       funFindAndChangeInt $1 $2 $CHANNEL_FILE_PATH_MERCHANT
       ;;
       MaiBaiCash)
       funFindAndChangeInt $1 $2 $CHANNEL_FILE_PATH_MAIBEI_CASH
       ;;

  esac
}
funFindAndChangeInt() {
    grepData=`grep  "$1"  $3`
      oldValue=`echo $grepData| cut -d \= -f 2`
#  echo "oldValue is " $oldValue
        sed -i ''  "s/$oldValue/ $2;/g" $3
}
funFindAndChangeString() {
    grepData=`grep  "$1"  $3`
      oldValue=`echo $grepData| cut -d \= -f 2`
#  echo "oldValue is " $oldValue
        sed -i ''  "s/$oldValue/ \"$2\";/g" $3
}
#./gradlew assembleRelease
rm -rf $currentApplicationName/build/outputs/apk/*

funBuild(){
    funChangeVersionCode
    funChangeVersionName
    funChangeOutApkName

    sed -i ''  "s/ceshi/$1/g" $currentApplicationName/build.gradle
    funFindAndChangeChannel   CHANNEL_ID $2

    ./gradlew assembleRelease

    sed -i ''  "s/$1/ceshi/g" $currentApplicationName/build.gradle
    funFindAndChangeChannel   CHANNEL_ID 1
}

#funBuild   ceshi                1
funBuild   server               2000
#funBuild   wandoujia            2001
#funBuild   qqTencent            2002
#funBuild   zs91zhushou          2003
#funBuild   huawei               2004
#funBuild   xiaomi               2005
#funBuild   baidu                2006
#funBuild   anzhi                2007
#funBuild   oppo                 2008
#funBuild   appchina             2009
#funBuild   lenovo               2010
#funBuild   meizu                2011
#funBuild   baidusem             2012
#funBuild   UCWeb                2013
#funBuild   dev360cn             2014
#funBuild   vivo                 2015



