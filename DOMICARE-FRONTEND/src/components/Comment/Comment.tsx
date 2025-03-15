import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from '../ui/carousel'
import CommentItem from './CommentItem'

export default function Comment() {
  return (
    <Carousel
      autoChange={true}
      autoChangeInterval={3000}
      opts={{
        align: 'start'
      }}
      className='w-full max-w-[250px] md:max-w-[650px] lg:max-w-[920px]'
    >
      <CarouselContent>
        {Array.from({ length: 5 }).map((_, index) => (
          <CarouselItem key={index} className='md:basis-1/2'>
            <CommentItem />
          </CarouselItem>
        ))}
      </CarouselContent>
      <CarouselPrevious />
      <CarouselNext />
    </Carousel>
  )
}
