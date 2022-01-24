package net.kikkirej.alexandria.analyzer.dockerfile

import net.kikkirej.alexandria.analyzer.dockerfile.config.GeneralProperties
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription
import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskHandler
import org.camunda.bpm.client.task.ExternalTaskService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File

@Component
@ExternalTaskSubscription("dockerfile-analysis")
class DockerFileAnalysis(@Autowired val generalProperties: GeneralProperties,
                         @Autowired val dockerFileCrawler: DockerFileCrawler,
                         @Autowired val dockerFileFromAnalyzer: DockerFileFromAnalyzer) : ExternalTaskHandler{

    val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun execute(externalTask: ExternalTask?, externalTaskService: ExternalTaskService?) {
        try {
            val analysisFolder = getAnalysisFolder(externalTask!!.businessKey)
            log.info("Analysis for '${externalTask.businessKey}'")
            log.debug("Analyzing in folder: $analysisFolder")
            val dockerfiles = dockerFileCrawler.searchIn(analysisFolder)
            log.info("Found files (unsure wether format is avaiable): $dockerfiles")
            for(dockerfile in dockerfiles){
                dockerFileFromAnalyzer.analyzeDockerFile(analysisFolder, dockerfile, externalTask.businessKey)
            }
            externalTaskService!!.complete(externalTask)
        }catch (exception: Exception){
            log.error("error while handling analysis '" + (externalTask?.businessKey ?: "unknown") + "'", exception)
            externalTaskService?.handleBpmnError(externalTask, "undefined", exception.message)
        }
    }

    private fun getAnalysisFolder(businessKey: String): File {
        val absoluteSharedFolderPath = File(generalProperties.sharedfolder).absolutePath
        val analysisFolderPath = absoluteSharedFolderPath + File.separator + businessKey
        return File(analysisFolderPath)
    }
}