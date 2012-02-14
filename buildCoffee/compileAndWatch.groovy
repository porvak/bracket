def workDir = "/opt/bracket/src/main/webapp/resources"
def cmd = "coffee --lint -o ${workDir}/js -cw ${workDir}/coffee"
cmd.execute()
println "Watching and compiling .coffee from ${workDir}/coffee to ${workDir}/js with JSLint checking."