package com.wonder.bring

class SizeConvertor {

    companion object {
        final val SAMLL = 0
        final val REGULAR = 1
        final val LARGE = 2

        final val DESSERT = 4
        final val OPTION = 5


        //String -> Int
        fun parseSizeInt(sizeString: String): Int {

            var returnInt: Int = -1

            when (sizeString) {
                "small", "SMALL", "Small" -> returnInt = 0
                "regular", "Regular", "REGULAR" -> returnInt = 1
                "large", "Large", "LARGE" -> returnInt = 2

                "dessert", "Dessert", "DESSERT" -> returnInt = 4
                "option", "Option", "OPTION" -> returnInt = 5
            }

            return returnInt
        }

        //Int -> String
        fun parseSizeString(sizeInt: Int): String {

            var returnString: String = "OTHER"

            when (sizeInt) {
                0 -> returnString = "Small"
                1 -> returnString = "Regular"
                2 -> returnString = "Large"

                4 -> returnString = "Dessert"
                5 -> returnString = "Option"
            }

            return returnString
        }
    }

}