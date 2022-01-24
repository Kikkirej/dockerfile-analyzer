package net.kikkirej.alexandria.analyzer.dockerfile.db

import org.springframework.data.repository.CrudRepository

interface AnalysisRepository : CrudRepository<Analysis, Long>{}

interface DockerImageRepository : CrudRepository<DockerImage, Long> {
    fun getByName(name: String) : DockerImage
}

interface DockerImageFileRepository : CrudRepository<DockerImageFile, Long> {}

interface DockerFileRepository : CrudRepository<DockerFile, Long> {}



