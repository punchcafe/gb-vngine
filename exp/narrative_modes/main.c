#include <gb/gb.h>
#include "./narrative_elements/text.c"
#include "./narrative_elements/foreground_element.c"
#include "./narrative_elements/pause.c"
#include "./narrative.c"
#include "./play_narrative.c"



unsigned char RHSLabel[] = {
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
  
  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,

  0xFF,0xFF,0x7F,0xFF,0xFE,0xFF,0xFF,0xFF,
  0xFF,0xFF,0xFF,0xFF,0xBF,0xFD,0xFF,0xFF,
};

unsigned char TileLabel[] =
{
  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,
  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

    0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,


  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7,

  0xC7,0xEF,0x0F,0xA7,0xE4,0xC3,0x40,0x80,
  0x02,0x01,0x27,0xC3,0xB1,0xE5,0xE3,0xF7
};

struct Text text_to_play_1 = {"hello world i thought youd come"};
struct Text text_to_play_2 = {"i knew it i knew it i knew it"};
struct Text text_to_play_3 = {"all according to keikaku"};

struct ForegroundElement fg1 = {LEFT, RHSLabel};
struct ForegroundElement fg2 = {RIGHT, TileLabel};
struct ForegroundElement fg3 = {CENTER, TileLabel};

struct Pause pause1 = {5};

struct NarrativeElement elem_1 = {&fg1, FOREGROUND};
struct NarrativeElement elem_2 = {&text_to_play_1, TEXT};
struct NarrativeElement elem_3 = {&fg2, FOREGROUND};
struct NarrativeElement elem_4 = {&text_to_play_2, TEXT};
struct NarrativeElement elem_5 = {&fg3, FOREGROUND};
struct NarrativeElement elem_6 = {0x00, CLEAR_TEXT};
struct NarrativeElement elem_7 = {&pause1, PAUSE};
struct NarrativeElement elem_8 = {&text_to_play_3, TEXT};

struct NarrativeElement * narratives [8] = {&elem_1, &elem_2, &elem_3, &elem_4, &elem_5, &elem_6, &elem_7, &elem_8};

struct Narrative narrative = {8, narratives};

int main()
{
    SPRITES_8x16;
    SHOW_SPRITES;
    initialise_font();
    SHOW_BKG;
    play_narrative(&narrative);
}