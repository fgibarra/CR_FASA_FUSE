#!/bin/sh
# the path to your PID file

PIDFILE=./pid/COBPEYA.pid
# the path to your  binary, including options if necessary
if [ "x"$JAVA_HOME = "x" ] ; then
#    JAVA_HOME=/usr/java/default
#    JAVA_HOME=/usr/java/jdk1.7.0_51
  JAVA_HOME=/usr/java/default

fi
JAVACMD=$JAVA_HOME/bin/java
DIR_HOME=/home/montran/runtimeCOBPEYA/

BIN_HOME=/home/montran/runtimeCOBPEYA
LOG_HOME=/home/montran/runtimeCOBPEYA/log

SERVICIO=-DCOBPEYAService
if [ ""$2 != "" ]; then
	SERVICIO="-D"$2
fi

# A function to find the pid of a program.

pidofproc() {
	ps x | awk 'BEGIN { prog=ARGV[1]; ARGC=1 }
                           { if($7 == prog)
			   {  print $1; exit 0 } }' $1
}

ERROR=0
ARGV="$@"
if [ "x$ARGV" = "x" ] ; then
    ARGS="help"
fi
#definir el CLASSPATH
oldCP=$CLASSPATH

if [ "$oldCP" != "" ]; then
    CLASSPATH=${CLASSPATH}:${oldCP}
fi

for ARG in $@ $ARGS
do
    # check for pidfile
    if [ -f $PIDFILE ] ; then
	PID=`cat $PIDFILE`
	if [ "x$PID" != "x" ] && kill -0 $PID 2>/dev/null ; then
	    STATUS="DCOBPEYAService (pid $PID) running"
	    RUNNING=1
	else
	    STATUS="DCOBPEYAService (pid $PID?) not running"
	    RUNNING=0
	fi
    else
        PID=`pidofproc $SERVICIO`
        if [ "x$PID" = "x" ]; then
		STATUS="DCOBPEYAService not running"
		RUNNING=0
	else
	    STATUS="DCOBPEYAService (pid $PID) running"
	    RUNNING=1
	fi
    fi

#echo CLASSPATH $CLASSPATH "servicio=$SERVICIO"

    case $ARG in
    start)
	if [ $RUNNING -eq 1 ]; then
	    echo "$0 $ARG: DCOBPEYAService (pid $PID) already running"
	    continue
	fi
	cd $LOG_HOME
        echo $LOG_HOME
        for j in `ls *.log` ; do
                mv $j $j.`date +%y%m%d%k%M%S`
        done
        cd $BIN_HOME
        $JAVACMD -jar $SERVICIO -Xms1024m -Xmx2048m -Dhttps.protocols=TLSv1.2 cobertura-peya-pos-proxy-1.0-jar-with-dependencies.jar &
        echo "$0 $ARG: DCOBPEYAService started."
	;;
    stop)
	if [ $RUNNING -eq 0 ]; then
	    echo "$0 $ARG: $STATUS"
	    continue
	fi
	if kill $PID ; then
	    echo "$0 $ARG: DCOBPEYAService stopped"
	else
	    echo "$0 $ARG: DCOBPEYAService could not be stopped"
	    ERROR=4
	fi
	;;
    restart)
	if [ $RUNNING -eq 0 ]; then
	    echo "$0 $ARG: DCOBPEYAService not running, trying to start"
            $JAVACMD -jar $SERVICIO -Xms1024m -Xmx2048m -Dhttps.protocols=TLSv1.2  COBPEYA-1.0-jar-with-dependencies.jar &
	else
            if kill $PID ; then
                echo "$0 $ARG: DCOBPEYAService stopped"
                sleep 30
            else
        	    echo "$0 $ARG: DCOBPEYAService could not be stopped"
	            ERROR=4
                    continue
	    fi
  	    cd $LOG_HOME
            for j in `ls *.log` ; do
                mv $j $j.`date +%y%m%d%k%M%S`
            done
	    cd $BIN_HOME
            $JAVACMD -jar $SERVICIO -Xms1024m -Xmx2048m -Dhttps.protocols=TLSv1.2 -Dpeya.status.ambiente=$AMBIENTE_EJECUCION -Dpeya.status.port=$PORT COBPEYA-1.0-jar-with-dependencies.jar &
            echo "$0 $ARG: DCOBPEYAService restarted"
	fi
	;;
    *)
	echo "usage: $0 (start|stop|restart|help)"
	cat <<EOF

start      - start DCOBPEYAService
stop       - stop DCOBPEYAService
restart    - restart DCOBPEYAService if running by sending a SIGHUP or start if
             not running
help       - this screen

EOF
	ERROR=2
    ;;

    esac

done

exit $ERROR

