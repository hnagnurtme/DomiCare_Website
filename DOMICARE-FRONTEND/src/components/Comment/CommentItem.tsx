import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'

export default function CommentItem() {
  return (
    <div className='bg-white p-6 rounded-lg max-w-md  shadow-md'>
      <p className='text-gray text-base mb-4 text-justify italic'>
        " Tôi năm nay hơn 20 tuổi mà tôi chừa gặp cái dịch vụ nào mà nó như thế này cả, phải tôi tôi book cho mấy phát."
      </p>
      <div className='flex items-center space-x-4'>
        <Avatar className='!size-10'>
          <AvatarImage src='https://github.com/shadcn.png' alt='@shadcn' />
          <AvatarFallback>CN</AvatarFallback>
        </Avatar>
        <div>
          <h3 className='text-lg font-semibold text-black'>Anh Trung Ánh</h3>
          <p className='text-sm text-gray'>Hoà Khánh</p>
        </div>
      </div>
    </div>
  )
}
