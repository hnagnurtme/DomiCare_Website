import { z } from 'zod'
import { validator } from '../helpers/validator'
import { numberConstants } from '@/configs/consts'

export const RegisterSchema = z
  .object({
    email: z
      .string()
      .min(numberConstants.TWO, {
        message: 'Email không được để trống.'
      })
      .email({
        message: 'Email không đúng định dạng.'
      }),
    password: z
      .string()
      .min(numberConstants.ONE, {
        message: 'Mật khẩu không được bỏ trống'
      })
      .regex(validator.passwordRegex, {
        message: 'Mật khẩu phải chứa ít nhất 6 kí tự bao gồm chữ in hoa in thường và chữ số.'
      }),
    confirmPassword: z
      .string()
      .min(numberConstants.ONE, {
        message: 'Nhập lại mật khẩu không được bỏ trống'
      })
      .regex(validator.passwordRegex, {
        message: 'Nhập lại mật khẩu phải chứa ít nhất 6 kí tự bao gồm chữ in hoa in thường và chữ số.'
      })
  })
  .superRefine(({ confirmPassword, password }, ctx) => {
    if (confirmPassword !== password) {
      ctx.addIssue({
        code: 'custom',
        message: 'Nhập lại mật khẩu không trùng khớp.',
        path: ['confirmPassword']
      })
    }
  })
