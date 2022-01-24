package net.kikkirej.alexandria.analyzer.dockerfile

import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import kotlin.collections.HashSet

@Service
class DockerFileCrawler {
    fun searchIn(analysisFolder: File) : Collection<File>{
        val dockerfiles = HashSet<File>()
        findFilesInDirAndAddToSet(analysisFolder, dockerfiles)
        return dockerfiles
    }

    private fun findFilesInDirAndAddToSet(analysisFolder: File, dockerfiles: java.util.HashSet<File>) {
        val files = analysisFolder.listFiles()
        for (file: File in files){
            if(file.isDirectory){
                findFilesInDirAndAddToSet(file, dockerfiles)
            }else if (file.name.lowercase().contains("dockerfile")){
                dockerfiles.add(file)
            }
        }
    }

}