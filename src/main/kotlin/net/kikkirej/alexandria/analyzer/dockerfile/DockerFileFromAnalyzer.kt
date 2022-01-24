package net.kikkirej.alexandria.analyzer.dockerfile

import net.kikkirej.alexandria.analyzer.dockerfile.db.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.util.*

@Component
class DockerFileFromAnalyzer(@Autowired val dockerImageRepository: DockerImageRepository,
                             @Autowired val analysisRepository: AnalysisRepository,
                             @Autowired val dockerFileRepository: DockerFileRepository, @Autowired val dockerImageFileRepository: DockerImageFileRepository) {

    fun analyzeDockerFile(rootDirectory: File, dockerFile: File, businessKey: String): Unit {
        val fromLines = getLinesContainingFrom(dockerFile)
        for(i in fromLines.indices){
            val fromLineWithoutKeyword = fromLines[i].replace("from ", "", true)
            val splitedFromLine = fromLineWithoutKeyword.split(" as ")
            var stagename: String? = null
            if(splitedFromLine.size ==2){
                stagename = splitedFromLine[1]
            }
            val imageWithTag = splitedFromLine[0].split(":")
            val imageName =  imageWithTag[0]
            var tag = "latest"
            if(imageWithTag.size==2){
                tag=imageWithTag[1]
            }
            var runimage = i == fromLines.size-1
            var dockerImage = getDockerImage(imageName)
            val analysis = analysisRepository.findById(businessKey.toLong())
            val dockerFileDb =
                DockerFile(analysis = analysis.get(), path = getSubPath(rootDirectory, dockerFile))
            dockerFileRepository.save(dockerFileDb)
            val dockerImageFile = DockerImageFile(
                dockerFile = dockerFileDb,
                dockerImage = dockerImage,
                index = i,
                runImage = runimage,
                stagename = stagename,
                tag = tag
            )
            dockerImageFileRepository.save(dockerImageFile)
        }
    }

    private fun getSubPath(rootDirectory: File, dockerFile: File): String {
        return dockerFile.absolutePath.replace(rootDirectory.absolutePath, "")
    }

    private fun getDockerImage(imageName: String): DockerImage {
        val dockerImageByName = dockerImageRepository.getByName(imageName)
        if(dockerImageByName!=null){
            return dockerImageByName
        }
        val dockerImage = DockerImage(name = imageName)
        dockerImageRepository.save(dockerImage)
        return dockerImage
    }

    private fun getLinesContainingFrom(dockerFile: File): List<String> {
        val lines = dockerFile.readLines()
        val result = LinkedList<String>()
        for (line in lines){
            if (line.lowercase().startsWith("from")){
                result.add(line)
            }
        }
        return result
    }
}