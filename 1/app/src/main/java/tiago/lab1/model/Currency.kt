package tiago.lab1.model

class Currency(val label: String, val rate: Float) {

    override fun toString(): String {
        return "Currency{" +
                "label='" + label +
                ", rate=" + rate +
                '}'.toString()
    }
}
