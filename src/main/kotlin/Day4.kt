import java.lang.Integer.parseInt
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    Files.readString(Paths.get("/tmp/input"))
        .split("\n\n")
        .map {
            it.split("\n")
                .flatMap { it.split(" ") }
                .filterNot { it.isEmpty() }
                .map {
                    it.split(":")
                        .let { it[0] to it[1] }
                }
                .toMap()
                .mapKeys { (key, _) -> Field.valueOf(key) }
        }
        .filter { map ->
            Field.values()
                .filterNot { it == Field.cid }
                .all { map.keys.contains(it) && it.validate(map[it]!!) }
        }
        .also {
            println(it)
        }
        .also {
            println(it.count())
        }
}

enum class Field {
    byr {
        override fun validate(s: String) =
            "\\d{4}+".toRegex().matches(s) &&
                    s.let { parseInt(s) }
                        .let {
                            it >= 1920 && it <= 2002
                        }
    },
    iyr {
        override fun validate(s: String) =
            "\\d{4}+".toRegex().matches(s) &&
                    s.let { parseInt(s) }
                        .let {
                            it >= 2010 && it <= 2020
                        }
    },
    eyr {
        override fun validate(s: String): Boolean =
            "\\d{4}+".toRegex().matches(s) &&
                    s.let { parseInt(s) }
                        .let {
                            it >= 2020 && it <= 2030
                        }
    },
    hgt {
        override fun validate(s: String): Boolean =
            ("\\d+cm".toRegex().matches(s) && parseInt(s.substringBefore("cm")).let { it >= 150 && it <= 193 }) ||
                    ("\\d+in".toRegex().matches(s) && parseInt(s.substringBefore("in")).let { it >= 59 && it <= 76 })
    },
    hcl {
        override fun validate(s: String): Boolean =
            "#[0-9a-f]{6}+".toRegex().matches(s)
    },
    ecl {
        override fun validate(s: String): Boolean =
            listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(s)
    },
    pid {
        override fun validate(s: String): Boolean =
            "\\d{9}+".toRegex().matches(s)
    },
    cid {
        override fun validate(s: String): Boolean = true
    };

    abstract fun validate(s: String): Boolean
}
