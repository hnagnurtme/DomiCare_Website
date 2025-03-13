import { pic7 } from '@/assets/images'

const HomePage = () => {
  return (
    <div className='w-full min-h-96 bg-secondary'>
      <div
        style={{ backgroundImage: `url('${pic7}')` }}
        className='bg-center min-h-[600px] bg-no-repeat bg-cover'
      ></div>
      <div className='max-w-7xl mx-auto p-4'>noidung</div>
    </div>
  )
}

export default HomePage
