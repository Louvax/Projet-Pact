from random import randint
import time
from rpi_ws281x import PixelStrip, Color

#-------------------------------------------------------------------------------------------

# LED strip configuration:
LED_COUNT = 10        # Number of LED pixels.
LED_PIN = 12          # GPIO pin connected to the pixels (18 uses PWM!).
# LED_PIN = 10        # GPIO pin connected to the pixels (10 uses SPI /dev/spidev0.0).
LED_FREQ_HZ = 800000  # LED signal frequency in hertz (usually 800khz)
LED_DMA = 10          # DMA channel to use for generating signal (try 10)
LED_BRIGHTNESS = 255  # Set to 0 for darkest and 255 for brightest
LED_INVERT = False    # True to invert the signal (when using NPN transistor level shift)
LED_CHANNEL = 0       # set to '1' for GPIOs 13, 19, 41, 45 or 53

#-------------------------------------------------------------------------------------------

def colorWipe(strip, color, wait_ms=50):
    """Wipe color across display a pixel at a time."""
    for i in range(strip.numPixels()):
        strip.setPixelColor(i, color)
        strip.show()
        time.sleep(wait_ms / 1000.0)

def theaterChase(strip, color, wait_ms=50, iterations=20):
    """Movie theater light style chaser animation."""
    for j in range(iterations):
        for q in range(3):
            for i in range(0, strip.numPixels(), 3):
                strip.setPixelColor(i + q, color)
            strip.show()
            time.sleep(wait_ms / 1000.0)
            for i in range(0, strip.numPixels(), 3):
                strip.setPixelColor(i + q, 0)


def read():
    # Create NeoPixel object with appropriate configuration.
    strip = PixelStrip(LED_COUNT, LED_PIN, LED_FREQ_HZ, LED_DMA, LED_INVERT, LED_BRIGHTNESS, LED_CHANNEL)
    # Intialize the library (must be called once before other functions).
    strip.begin()

    # print('Press Ctrl-C to quit.')

    try:
        while True:
            i=randint(0,4)
            if i == 0:
                colorWipe(strip,Color(0,0,255)) #blue wipe
                colorWipe(strip,Color(0,0,255))
            elif i== 1:
                theaterChase(strip, Color(0,0,255)) #blue theaterchase
            # elif i == 2:
            #     rainbowCycle(strip)
            # else:
            #     theaterChaseRainbow(strip)

    finally:
        colorWipe(strip, Color(0, 0, 0), 10)
