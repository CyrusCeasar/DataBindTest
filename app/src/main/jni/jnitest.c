#include <jni.h>
#include <android/log.h>
#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

#define TAG "NativeCodec"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)

//extern **environ;
void Java_com_example_chenlei2_databindtest_model_JniTest_sayHello(JNIEnv *env, jclass clazz) {
    LOGV("hello world");
}


void Java_com_example_chenlei2_databindtest_model_JniTest_priEnvInfo(JNIEnv *env, jclass clazz) {
    /* char **en = environ;
     while(*en){
         LOGV("%s\n",*en);
         en++;
     }*/
}


void Java_com_example_chenlei2_databindtest_model_JniTest_fork(JNIEnv *env, jclass clazz) {
}

/*  pid_t pid;
  char *message;
  int n;
  LOGV("fork program starting \n");

//    pid = fork();
  LOGV("fork program success %d \n", pid);
  switch (pid) {
      case -1:
          LOGV("fork failed");
      case 0:
          message = "This is the child";
          LOGV("fork success: %s \n", message);

          n = 5;
          break;
      default:
          message = "This is the parent";
          LOGV("fork success: %s \n", message);
          n = 3;
          break;
  }

  for (; n > 0; n--) {
      LOGV("fork success: %s \n", message);
  }*/
void Java_com_example_chenlei2_databindtest_model_JniTest_showTime(JNIEnv *env, jclass clazz) {
    int i;
    time_t the_time;
    for (i = 1; i <= 10; i++) {
        the_time = time((time_t *) 0);
        LOGV("The time is %ld\n", the_time);
        sleep(0.1);
    }
}
void Java_com_example_chenlei2_databindtest_model_JniTest_showGTime(JNIEnv *env, jclass clazz) {
    int i;
    time_t the_time;
    for (i = 1; i <= 10; i++) {
        the_time = time((time_t *) 0);
        LOGV("The time is %ld\n", the_time);
        sleep(0.1);
    }
}