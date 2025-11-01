export interface CommentResponse {
  id: number;
  message: string;
  role: 'USER' | 'AI';
  timestamp: string;
  reviewId: number;
  userId: number;
}

