package com.medarkive.Beans;

public class EbookChapterBean
{
  String chapterId;
  String chapterName;
  String chapterTitle;

  public EbookChapterBean()
  {
  }

  public EbookChapterBean(String paramString1, String paramString2, String paramString3)
  {
    this.chapterTitle = paramString1;
    this.chapterName = paramString2;
    this.chapterId = paramString3;
  }

  public String getChapterId()
  {
    return this.chapterId;
  }

  public String getChapterName()
  {
    return this.chapterName;
  }

  public String getChapterTitle()
  {
    return this.chapterTitle;
  }

  public void setChapterId(String paramString)
  {
    this.chapterId = paramString;
  }

  public void setChapterName(String paramString)
  {
    this.chapterName = paramString;
  }

  public void setChapterTitle(String paramString)
  {
    this.chapterTitle = paramString;
  }
}

/* Location:           /home/sam/Desktop/com.medarkive-1/Untitled Folder/classes_dex2jar.jar
 * Qualified Name:     com.medarkive.Beans.EbookChapterBean
 * JD-Core Version:    0.6.0
 */