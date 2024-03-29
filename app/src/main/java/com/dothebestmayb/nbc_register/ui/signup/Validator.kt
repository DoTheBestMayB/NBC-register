package com.dothebestmayb.nbc_register.ui.signup

import java.text.BreakIterator

class Validator {

    private val validPwLength = 8..16
    private val breakIterator = BreakIterator.getCharacterInstance()
    private val disallowedSpecialCharacterRegex = Regex("[^@#!\\w]")

    fun checkLength(input: String): Boolean = countLengthOfLetter(input) in validPwLength

    private fun countLengthOfLetter(input: String): Int {
        breakIterator.setText(input)

        var count = 0
        while (breakIterator.next() != BreakIterator.DONE) {
            count++
        }
        return count
    }

    fun checkContainCapital(pw: String): Boolean {
        return pw.any { it.isUpperCase() }
    }

    fun checkContainAtLeastOneSpecialCharacter(pw: String): Boolean {
        return pw.any { it in allowedSpecialCharacter }
    }

    fun checkContainNotAllowedSpecialCharacter(pw: String): Boolean {
        return disallowedSpecialCharacterRegex.containsMatchIn(pw)
    }

    companion object {
        val allowedSpecialCharacter = listOf('@', '#', '!')
    }
}