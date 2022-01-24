package net.kikkirej.alexandria.analyzer.dockerfile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DockerFileAnalyzerApplication

fun main(args: Array<String>) {
	runApplication<DockerFileAnalyzerApplication>(*args)
}
