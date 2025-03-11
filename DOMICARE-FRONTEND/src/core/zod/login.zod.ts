import { numberConstants } from '@/configs/consts'
import { z } from 'zod'

export const LoginSchema = z.object({
  email: z
    .string()
    .min(numberConstants.TWO, {
      message: 'Email không được để trống.'
    })
    .email({
      message: 'Email không đúng định dạng.'
    }),
  password: z.string().min(numberConstants.SIX, {
    message: 'Mật khẩu phải có ít nhất 6 kí tự.'
  })
})
