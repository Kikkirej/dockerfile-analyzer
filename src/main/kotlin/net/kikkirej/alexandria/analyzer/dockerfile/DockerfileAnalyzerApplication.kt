package net.kikkirej.alexandria.analyzer.dockerfile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DockerfileAnalyzerApplication

fun main(args: Array<String>) {
	runApplication<DockerfileAnalyzerApplication>(*args)
}
