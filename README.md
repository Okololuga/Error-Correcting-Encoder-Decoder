# Error-Correcting-Encoder-Decoder

Hyperskill project

Errors are inevitable both in life and in the digital world. Errors occur here and there and everywhere, and in this project you will not only imitate this process, but also learn how to cope with errors. It is a chance to experience what early developers had to cope with at the dawn of the computer era. Low-level programming is fun and insightful: try it and you’ll see.

Examples
====================================================================================
Write a mode: encode
 
send.txt:
text view: Test
hex view: 54 65 73 74
bin view: 01010100 01100101 01110011 01110100
 
encoded.txt:
expand: ..0.101. ..0.100. ..0.110. ..0.101. ..0.111. ..0.011. ..0.111. ..0.100.
parity: 01001010 10011000 11001100 01001010 00011110 10000110 00011110 10011000
hex view: 4A 98 CC 4A 1E 86 1E 98
====================================================================================

Write a mode: send
 
encoded.txt:
hex view: 4A 98 CC 4A 1E 86 1E 98
bin view: 01001010 10011000 11001100 01001010 00011110 10000110 00011110 10011000
 
received.txt:
bin view: 01011010 10001000 10001100 01001110 00010110 10100110 00111110 10010000
hex view: 5A 88 8C 4E 16 A6 3E 90
====================================================================================

Write a mode: decode
 
received.txt:
hex view: 5A 88 8C 4E 16 A6 3E 90
bin view: 01011010 10001000 10001100 01001110 00010110 10100110 00111110 10010000
 
decoded.txt:
correct: 01001010 10011000 11001100 01001010 00011110 10000110 00011110 10011000
decode: 01010100 01100101 01110011 01110100
hex view: 54 65 73 74
text view: Test
====================================================================================
