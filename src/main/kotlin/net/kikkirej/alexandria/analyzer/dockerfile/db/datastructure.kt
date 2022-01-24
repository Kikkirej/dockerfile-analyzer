package net.kikkirej.alexandria.analyzer.dockerfile.db

import java.io.Serializable
import javax.persistence.*

@Entity(name = "analysis")
class Analysis(@Id var id: Long)

@Entity(name="docker_file")
class DockerFile(@Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
                 @ManyToOne var analysis: Analysis,
                 var path: String) : Serializable

@Entity(name = "docker_image_file")
class DockerImageFile(@Id @ManyToOne var dockerFile: DockerFile,
                      @Id @ManyToOne var dockerImage: DockerImage,
                      @Id var index: Int,
                      var runImage: Boolean,
                      var stagename: String?,
                      var tag: String?) : Serializable

@Entity(name = "docker_image")
class DockerImage(@Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
                  @Column(unique = true, nullable = false) var name: String) : Serializable
