@REM ----------------------------------------------------------------------------
@REM Jargyle Start Up Batch script to run jargyle.cmd with the lib dir set to
@REM target
@REM ----------------------------------------------------------------------------

@setlocal
@set JARGYLE_LIB=target

@call "%~dp0"jargyle.cmd %*