task coffee {
    def workDir = "/opt/bracket/src/main/webapp/resources"
    def cmd = "coffee.cmd --lint -o ${workDir}/js -c ${workDir}/coffee".execute()
    println "Compiling .coffee from ${workDir}/coffee to ${workDir}/js with JSLint checking."
}