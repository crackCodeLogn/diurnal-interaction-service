APP_NAME="diurnal-interaction-service"
APP_VERSION="0.0.2-SNAPSHOT"
JAVA_PARAM="-Xmx101m"
APP_PARAMS="-DPROFILE=local -DMAIL_DAILY_BILLZ=$DIURNAL_MAIL -DMAIL_DAILY_BILLZ_CREDENTIAL=$DIURNAL_MAIL_CRED"

BIN_PATH=$PROM_HOME_PARENT/diurnal/$APP_NAME/bin     #PROM-HOME-PARENT :: exported in .bashrc
JAR_PATH=$BIN_PATH/../target/quarkus-app/quarkus-run.jar

echo "Starting '$APP_NAME' with java param: '$JAVA_PARAM', app params: '$APP_PARAMS' at '$JAR_PATH'"
java $JAVA_PARAM $APP_PARAMS -jar $JAR_PATH
