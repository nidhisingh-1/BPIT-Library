package com.example.nidhi.bpit_library

class BookModel(
    val imageId: Int,
    val title: String,
    val description: String,
    val BooksAvailable: String,
    val id: Int? = null
) {
    companion object {
        fun getalltheBooks(): ArrayList<BookModel> = arrayListOf(

            BookModel(
                R.drawable.algo_to_live_by,
                "Algo to live by",
                "A fascinating exploration of how insights from computer algorithms can be applied to our everyday lives, helping to solve common decision-making problems and illuminate the workings of the human mind",
                "10"
            ),
            BookModel(
                R.drawable.c_programming,
                "THE C PROGRAMMING LANGUAGE",
                "C Programming Language 2nd Edition is a book that gives you just what you need to know about this powerful programming language. ",
                "9"
            ),
            BookModel(
                R.drawable.new_machine,
                "The Soul of A New Machine",
                "Tracy Kidder's riveting story of one company's efforts to bring a new microcomputer to market won both the Pulitzer Prize and the National Book Award and has...",
                "19"
            ),
            BookModel(
                R.drawable.art_programming,
                "Art of Computer Programming",
                "The bible of all fundamental algorithms and the work that taught many of today's software developers most of what they know about computer programming.",
                "4"
            ),
            BookModel(
                R.drawable.super_intelligence,
                "Superintelligence",
                "The human brain has some capabilities that the brains of other animals lack.",
                "6"
            ),
            BookModel(
                R.drawable.design_patterns,
                "Design Patterns",
                "Capturing a wealth of experience about the design of object-oriented software.",
                "9"
            ),
            BookModel(
                R.drawable.computer_networks,
                "Computer Networking",
                "Building on the successful top-down approach of previous editions",
                "67"
            ),
            BookModel(
                R.drawable.artificial_intelligence,
                "The Artificial Intelligence",
                "The long-anticipated revision of this #1 selling book offers the most comprehensive...",
                "27"
            ),
        )
    }
}
