package protocol;

public enum Event {
	ConnectingToServer,
	ConnectingToServerACK,
	UploadFile,
	UploadFileACK,
	ListFilesCanDownload,
	ListFilesCanDownloadACK,
	DownloadFile,
	DownloadFileACK,
	Nop,	// write at the end
	WantChunkTh,
	EndSharingFile
}
