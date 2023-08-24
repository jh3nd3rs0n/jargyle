@REM ----------------------------------------------------------------------------
@REM Jargyle Start Up Batch script
@REM
@REM Required ENV variables:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV variables
@REM JARGYLE_HOME - location of Jargyle's installed home dir
@REM JARGYLE_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM JARGYLE_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM JARGYLE_OPTS - parameters passed to the Java VM when running Jargyle
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case JARGYLE_BATCH_ECHO is 'on'
@echo off
@REM enable echoing my setting JARGYLE_BATCH_ECHO to 'on'
@if "%JARGYLE_BATCH_ECHO%" == "on"  echo %JARGYLE_BATCH_ECHO%

@setlocal

set ERROR_CODE=0

@REM To isolate internal variables from possible post scripts, we use another setlocal
@setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJavaHome

echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJavaHome
if exist "%JAVA_HOME%\bin\java.exe" goto checkJargyleHome

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:checkJargyleHome
if not "%JARGYLE_HOME%"=="" goto validateJargyleHome

SET "JARGYLE_HOME=%~dp0.."
if not "%JARGYLE_HOME%"=="" goto validateJargyleHome

echo.
echo Error: JARGYLE_HOME not found in your environment. >&2
echo Please set the JARGYLE_HOME variable in your environment to match the >&2
echo location of the Jargyle installation. >&2
echo.
goto error

:validateJargyleHome

:stripJargyleHome
if not "_%JARGYLE_HOME:~-1%"=="_\" goto checkJargyleCmd
set "JARGYLE_HOME=%JARGYLE_HOME:~0,-1%"
goto stripJargyleHome

:checkJargyleCmd
if exist "%JARGYLE_HOME%\bin\jargyle.cmd" goto constructJargyleClasspath

echo.
echo Error: JARGYLE_HOME is set to an invalid directory. >&2
echo JARGYLE_HOME = "%JARGYLE_HOME%" >&2
echo Please set the JARGYLE_HOME variable in your environment to match the >&2
echo location of the Jargyle installation >&2
echo.
goto error
@REM ==== END VALIDATION ====

:constructJargyleClasspath

@setlocal EnableDelayedExpansion
for %%i in ("%JARGYLE_HOME%"\lib\*.jar) do set JARGYLE_CLASSPATH=!JARGYLE_CLASSPATH!%%i;
@endlocal & set JARGYLE_CLASSPATH=%JARGYLE_CLASSPATH%

if not "%JARGYLE_CLASSPATH%"=="" goto init

echo.
echo Error: Unable to find jar files in '%JARGYLE_HOME%\lib' 
echo.
goto error

:init

set JARGYLE_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

set JARGYLE_MAIN_CLASS=com.github.jh3nd3rs0n.jargyle.cli.JargyleCLI

set JARGYLE_CMD_LINE_ARGS=%*

%JARGYLE_JAVA_EXE% %JARGYLE_OPTS% -classpath %JARGYLE_CLASSPATH% -Dprogram.name=jargyle %JARGYLE_MAIN_CLASS% %JARGYLE_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

@REM pause the script if JARGYLE_BATCH_PAUSE is set to 'on'
if "%JARGYLE_BATCH_PAUSE%" == "on" pause

if "%JARGYLE_TERMINATE_CMD%" == "on" exit %ERROR_CODE%

exit /B %ERROR_CODE%
