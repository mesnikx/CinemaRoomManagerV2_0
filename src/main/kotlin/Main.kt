class Cinema(private val rows: Int, private val seats: Int) {
    private var ticketCounter = 0
    private var currentIncome = 0
    private var seatsStatus: MutableList<MutableList<Char>> = mutableListOf()

    init {
        for (i in 0 until rows) {
            val row = MutableList(seats) { 'S' }
            seatsStatus.add(row)
        }
    }

    fun buyTicket() {
        println("Enter a row number: ")
        val rowTicket = readln().toInt()
        println("Enter a seat number in that row: ")
        val seatTicket = readln().toInt()

        try {
            if (rowTicket > rows || seatTicket > seats) {
                throw Exception("Wrong input!")
            }
            if (seatsStatus[rowTicket - 1][seatTicket - 1] == 'B') {
                throw Exception("That ticket has already been purchased!")
            }
        } catch (e: Exception) {
            println(e.message)
            println(" ")
            return buyTicket()
        }

        val ticketPrice: Int = if (rows * seats < 60) {
            10
        } else {
            if (rowTicket <= rows / 2) {
                10
            } else {
                8
            }
        }

        println("Ticket price: $$ticketPrice")
        seatsStatus[rowTicket - 1][seatTicket - 1] = 'B'
        ticketCounter += 1
        currentIncome += ticketPrice
        println(" ")
    }

    fun showTheSeats() {
        println(" ")
        println("Cinema: ")
        val list = mutableListOf<Int>()
        for (i in 0 until seats) {
            list.add(i + 1)
        }
        print("  ")
        println(list.joinToString(" "))
        var counter = 1
        for (row in seatsStatus) {
            print("$counter ")
            println(row.joinToString(" "))
            counter++
        }
    }

    fun statistics() {
        println("Number of purchased tickets: $ticketCounter")
        val percentage = (ticketCounter.toFloat() / (rows * seats) * 100)
        val formatPercentage = "%.2f".format(percentage)
        println("Percentage: $formatPercentage%")
        println("Current income: $$currentIncome")
        println(
            "Total income: $${
                if (seats * rows < 60) rows * seats * 10
                else (seats * (rows / 2) * 10) + (seats * (rows - rows / 2) * 8)
            }"
        )
        println(" ")
    }
    fun findSeatLocation(ticketNumber: Int) {
        if (ticketNumber > rows * seats) {
            println("Invalid ticket number.")
            return
        }

        val row = (ticketNumber - 1) / seats
        val seat = (ticketNumber - 1) % seats

        println("Seat location for ticket $ticketNumber: Row ${row + 1}, Seat ${seat + 1}")
    }

    fun refundTicket() {
        println("Enter the row number:")
        val row = readln().toInt()
        println("Enter the seat number:")
        val seat = readln().toInt()

        if (row !in 1..rows || seat !in 1..seats) {
            println("Invalid seat location.")
            return
        }

        if (seatsStatus[row - 1][seat - 1] == 'S') {
            println("That seat has not been purchased.")
            return
        }

        val ticketPrice = if (rows * seats < 60 || row <= rows / 2) 10 else 8

        seatsStatus[row - 1][seat - 1] = 'S'
        ticketCounter -= 1
        currentIncome -= ticketPrice

        println("Ticket refunded.")
    }


}

class CinemaManager(private val cinema: Cinema) {

    fun menu() {
        println(
            """
            1. Show the seats
            2. Buy a ticket
            3. Statistics
            4. Find seat location by ticket number
            5. Refund a ticket
            0. Exit
        """.trimIndent()
        )
        when (readln().toInt()) {
            1 -> cinema.showTheSeats()
            2 -> cinema.buyTicket()
            3 -> cinema.statistics()
            4 -> {
                println("Enter ticket number:")
                val ticketNumber = readln().toInt()
                cinema.findSeatLocation(ticketNumber)
            }
            5 -> cinema.refundTicket()
            0 -> return
            else -> {
                println("Invalid option, please try again.")
                menu()
            }
        }
        menu()
    }
}


fun main() {
    println("Enter the number of rows:")
    val rows = readln().toInt()
    println("Enter the number of seats in each row:")
    val seats = readln().toInt()
    val cinema = Cinema(rows, seats)
    val manager = CinemaManager(cinema)
    manager.menu()
}