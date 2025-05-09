package com.bunbeauty.tiptoplive.features.stream.data.store

private const val AVATAR_COUNT = 100

class AvatarStore {

    private var nextIndex = 0
    private var pictureNames = generatePictureNames()

    fun getNext(): String {
        val comment = pictureNames[nextIndex]

        val newIndex = nextIndex + 1
        nextIndex = newIndex % pictureNames.size

        if (newIndex > pictureNames.size) {
            pictureNames = generatePictureNames()
        }

        return comment
    }

    private fun generatePictureNames(): List<String> {
        return (1..AVATAR_COUNT)
            .shuffled()
            .map { i ->
                "a$i"
            }
    }

}