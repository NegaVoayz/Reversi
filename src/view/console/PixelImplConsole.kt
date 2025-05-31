package view.console

class PixelImplConsole : Pixel {
    // use string to contain multibyte character
    private var data: String
    private var lastPainted: String

    /**
     * blank by default
     */
    constructor() {
        data = " "
        lastPainted = ""
    }

    /**
     * @param character multibyte character
     */
    constructor(character: String) {
        data = character
        lastPainted = ""
    }

    /**
     * @param character normal one-byte character
     */
    constructor(character: Char) {
        data = "" + character
        lastPainted = ""
    }

    /**
     * set data using another pixel
     *
     * @param pix the pixel 'color'
     * @return 1 if data changed, 0 if not changed, -1 if changed back
     */
    override fun set(pix: Pixel): Int {
        // try to convert pixel
        require(pix is PixelImplConsole) { "Invalid Pixel implementation" }
        // lazy check
        if (this.lastPainted == pix.data) {
            if (this.data == pix.data) {
                return 0
            } else {
                this.data = pix.data
                return -1
            }
        } else {
            this.data = pix.data
            return 1
        }
    }

    /**
     * @return character to be paint
     */
    fun get(): String {
        return data
    }

    override fun paint(): String {
        return data
    }

    /**
     * flush buffer
     */
    override fun flush() {
        lastPainted = data
        return
    }
}