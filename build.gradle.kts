plugins {
    id("fpgradle-minecraft") version ("0.11.0")
}

group = "com.falsepattern"

minecraft_fp {
    mod {
        modid = "laggoggles"
        name = "LagGoggles"
        rootPkg = "$group.laggoggles"
    }
    mixin {
        pkg = "mixin.mixins"
        pluginClass = "mixin.plugin.MixinPlugin"
    }

    core {
        accessTransformerFile = "laggoggles_at.cfg"
    }

    tokens {
        tokenClass = "Tags"
    }

    publish {
        changelog = "https://github.com/myname/mymod/releases/tag/$version"
        maven {
            repoUrl = "https://mvn.falsepattern.com/releases"
            repoName = "mavenpattern"
        }
        curseforge {
            projectId = "886297"
            dependencies {
                required("fplib")
            }
        }
        modrinth {
            projectId = "yNnilXec"
            dependencies {
                required("fplib")
            }
        }
    }
}

repositories {
    cursemavenEX()
    exclusive(mavenpattern(), "com.falsepattern")
}

dependencies {
    implementation("com.falsepattern:falsepatternlib-mc1.7.10:1.2.4:dev")
    compileOnly(deobfCurse("dragonapi-235591:4722480"))
}
